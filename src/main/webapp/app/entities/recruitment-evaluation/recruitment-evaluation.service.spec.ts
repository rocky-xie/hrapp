import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { RecruitmentEvaluation } from '@/shared/model/recruitment-evaluation.model';

import RecruitmentEvaluationService from './recruitment-evaluation.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('RecruitmentEvaluation Service', () => {
    let service: RecruitmentEvaluationService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new RecruitmentEvaluationService();
      currentDate = new Date();
      elemDefault = new RecruitmentEvaluation(
        123,
        'RESUME_SCREENING',
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        0,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          evaluatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          submittedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a RecruitmentEvaluation', async () => {
        const returnedFromService = {
          id: 123,
          evaluatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          submittedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { evaluatedAt: currentDate, submittedAt: currentDate, publishedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a RecruitmentEvaluation', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a RecruitmentEvaluation', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          activityId: 1,
          templateVersionId: 1,
          evaluatorUsername: 'BBBBBB',
          recordedByUsername: 'BBBBBB',
          status: 'BBBBBB',
          overallRecommendation: 'BBBBBB',
          totalScore: 1,
          summary: 'BBBBBB',
          answersJson: 'BBBBBB',
          evaluatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          submittedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          correctionOfId: 1,
          ...elemDefault,
        };

        const expected = { evaluatedAt: currentDate, submittedAt: currentDate, publishedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a RecruitmentEvaluation', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a RecruitmentEvaluation', async () => {
        const patchObject = {
          stageType: 'BBBBBB',
          evaluatorUsername: 'BBBBBB',
          recordedByUsername: 'BBBBBB',
          status: 'BBBBBB',
          overallRecommendation: 'BBBBBB',
          totalScore: 1,
          summary: 'BBBBBB',
          answersJson: 'BBBBBB',
          evaluatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          correctionOfId: 1,
          ...new RecruitmentEvaluation(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { evaluatedAt: currentDate, submittedAt: currentDate, publishedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a RecruitmentEvaluation', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of RecruitmentEvaluation', async () => {
        const returnedFromService = {
          stageType: 'BBBBBB',
          activityId: 1,
          templateVersionId: 1,
          evaluatorUsername: 'BBBBBB',
          recordedByUsername: 'BBBBBB',
          status: 'BBBBBB',
          overallRecommendation: 'BBBBBB',
          totalScore: 1,
          summary: 'BBBBBB',
          answersJson: 'BBBBBB',
          evaluatedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          submittedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          publishedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          correctionOfId: 1,
          ...elemDefault,
        };
        const expected = { evaluatedAt: currentDate, submittedAt: currentDate, publishedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of RecruitmentEvaluation', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a RecruitmentEvaluation', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a RecruitmentEvaluation', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
