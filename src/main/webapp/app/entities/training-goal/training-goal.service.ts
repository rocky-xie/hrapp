import axios from 'axios';

import { type ITrainingGoal } from '@/shared/model/training-goal.model';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

const baseApiUrl = 'api/training-goals';

export default class TrainingGoalService {
  find(id: number): Promise<ITrainingGoal> {
    return new Promise<ITrainingGoal>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  create(entity: ITrainingGoal): Promise<ITrainingGoal> {
    return new Promise<ITrainingGoal>((resolve, reject) => {
      axios
        .post(baseApiUrl, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  update(entity: ITrainingGoal): Promise<ITrainingGoal> {
    return new Promise<ITrainingGoal>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  partialUpdate(entity: ITrainingGoal): Promise<ITrainingGoal> {
    return new Promise<ITrainingGoal>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  complete(id: number): Promise<ITrainingGoal> {
    return new Promise<ITrainingGoal>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}/${id}/complete`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
