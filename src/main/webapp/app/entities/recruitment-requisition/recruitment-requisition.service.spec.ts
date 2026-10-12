import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { RecruitmentRequisition } from '@/shared/model/recruitment-requisition.model';

import RecruitmentRequisitionService from './recruitment-requisition.service';

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
  describe('RecruitmentRequisition Service', () => {
    let service: RecruitmentRequisitionService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new RecruitmentRequisitionService();
      currentDate = new Date();
      elemDefault = new RecruitmentRequisition(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        'GRADUATE',
        'PREPARING',
        'EDITABLE',
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        0,
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          targetJoinDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredStartDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredEndDate: dayjs(currentDate).format(DATE_FORMAT),
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          unlockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a RecruitmentRequisition', async () => {
        const returnedFromService = {
          id: 123,
          targetJoinDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredStartDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredEndDate: dayjs(currentDate).format(DATE_FORMAT),
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          unlockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = {
          targetJoinDate: currentDate,
          desiredStartDate: currentDate,
          desiredEndDate: currentDate,
          lockedAt: currentDate,
          unlockedAt: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a RecruitmentRequisition', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a RecruitmentRequisition', async () => {
        const returnedFromService = {
          requisitionCode: 'BBBBBB',
          title: 'BBBBBB',
          department: 'BBBBBB',
          positionId: 1,
          recruitmentType: 'BBBBBB',
          lifecycle: 'BBBBBB',
          editStatus: 'BBBBBB',
          plannedHeadcount: 1,
          hiredCount: 1,
          checkedInCount: 1,
          priority: 'BBBBBB',
          reason: 'BBBBBB',
          targetJoinDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredStartDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredEndDate: dayjs(currentDate).format(DATE_FORMAT),
          currentVersion: 1,
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lockedBy: 'BBBBBB',
          unlockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          unlockReason: 'BBBBBB',
          requirementsJson: 'BBBBBB',
          ...elemDefault,
        };

        const expected = {
          targetJoinDate: currentDate,
          desiredStartDate: currentDate,
          desiredEndDate: currentDate,
          lockedAt: currentDate,
          unlockedAt: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a RecruitmentRequisition', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a RecruitmentRequisition', async () => {
        const patchObject = {
          positionId: 1,
          lifecycle: 'BBBBBB',
          editStatus: 'BBBBBB',
          checkedInCount: 1,
          priority: 'BBBBBB',
          targetJoinDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredStartDate: dayjs(currentDate).format(DATE_FORMAT),
          currentVersion: 1,
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          unlockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...new RecruitmentRequisition(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          targetJoinDate: currentDate,
          desiredStartDate: currentDate,
          desiredEndDate: currentDate,
          lockedAt: currentDate,
          unlockedAt: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a RecruitmentRequisition', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of RecruitmentRequisition', async () => {
        const returnedFromService = {
          requisitionCode: 'BBBBBB',
          title: 'BBBBBB',
          department: 'BBBBBB',
          positionId: 1,
          recruitmentType: 'BBBBBB',
          lifecycle: 'BBBBBB',
          editStatus: 'BBBBBB',
          plannedHeadcount: 1,
          hiredCount: 1,
          checkedInCount: 1,
          priority: 'BBBBBB',
          reason: 'BBBBBB',
          targetJoinDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredStartDate: dayjs(currentDate).format(DATE_FORMAT),
          desiredEndDate: dayjs(currentDate).format(DATE_FORMAT),
          currentVersion: 1,
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lockedBy: 'BBBBBB',
          unlockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          unlockReason: 'BBBBBB',
          requirementsJson: 'BBBBBB',
          ...elemDefault,
        };
        const expected = {
          targetJoinDate: currentDate,
          desiredStartDate: currentDate,
          desiredEndDate: currentDate,
          lockedAt: currentDate,
          unlockedAt: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of RecruitmentRequisition', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a RecruitmentRequisition', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a RecruitmentRequisition', async () => {
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
